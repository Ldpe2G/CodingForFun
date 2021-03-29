// There are roughly three parts to compute einsum:
// 1. Parse equation to extract the labels for each input operand and output
// 2. Unsqueeze missing dimensions from input operands and permute to align them
// 3. Compute result by multiplying input operands and summing contraction
//    dimensions We do the last part by reducing to bmm.


// 为了让以下代码解读更加容易理解，省略了对于 "..." 省略号的处理

/** 
 * 代码实现主要分为3大步：
 * 1. 解析 equation 字符串，分别得到输入和输出字符串的内容
 * 2. 补全输出和输入张量的维度，然后通过 permute 操作对齐输入和输出的维度
 * 3. 将维度对齐之后的输入张量相乘，然后累加
*/
Tensor einsum(std::string equation, TensorList operands) {
  // ......
  // 把 equation 按照 -> 分割， 得到 -> 左边输入的部分
  const auto arrow_pos = equation.find("->");
  const auto lhs = equation.substr(0, arrow_pos);
  // 输入操作数个数
  const auto num_ops = operands.size();

  // 下面循环主要作用是解析 equation 左边输入部分，
  // 按 ',' 号分割得到每个输入张量对应的字符串，
  // 并且把字符串按字符分割，并把 char 字符转成 int 范围 [0, 25] 
  std::vector<std::vector<int>> op_labels(num_ops); // 保存每个输入张量对应的字符数组
  std::size_t curr_op = 0;
  for (auto i = decltype(lhs.length()){0}; i < lhs.length(); ++i) {
    switch (lhs[i]) {
      // ......
      case ',': 
        // 遇到逗号，接下来就是解析下一个输入张量的字符串
        ++curr_op;
        // ......
        break;
      default:
        // ......
        // 把 char 字符转成 int 
        op_labels[curr_op].push_back(lhs[i] - 'a');
    }
  }

  // TOTAL_LABELS = 26
  constexpr int TOTAL_LABELS = 'z' - 'a' + 1;
  std::vector<int> label_count(TOTAL_LABELS, 0); 
  // 遍历所有输入操作数
  // 统计 equation 中 'a' - 'z' 每个字符的出现次数
  for(const auto i : c10::irange(num_ops)) {
    const auto operand = operands[i];
    const auto labels = op_labels[i];
    const int64_t ndims = operand.dim();
    int64_t nlabels = labels.size();
    for (const auto& label : labels) {
      // ......
      ++label_count[label];
    }
    // ......
  }

  // 对齐输出张量的维度，使得对齐之后的维度等于
  // 输出的维度加上输入求和的维度
  // 创建一个 vector 用于保存字符到索引的映射
  std::vector<int64_t> label_perm_index(TOTAL_LABELS, -1);

  // Current index in the permuted shape
  int64_t perm_index = 0;
  // ......
  if (arrow_pos == std::string::npos) {
    // 用户没省略了 -> 和输出等式的情况，
    // ......
  } else {
    // 一般情况
    // 得到 -> 右边的输出等式
    const auto rhs = equation.substr(arrow_pos + 2);
    // 遍历输出等式并解析
    for (auto i = decltype(rhs.length()){0}; i < rhs.length(); ++i) {
      switch (rhs[i]) {
        // ......
        default:
          // ......
          const auto label = rhs[i] - 'a';
          // ......
          // 建立字符到索引的映射，perm_index从0开始
          label_perm_index[label] = perm_index++;
      }
    }
  }

  // 保存原始的输出维度大小
  const int64_t out_size = perm_index;
  // 对输出等式补全省略掉的求和索引
  // 也就是在输入等式中出现，但是没有在输出等式中出现的字符
  for (const auto label : c10::irange(TOTAL_LABELS)) {
    if (label_count[label] > 0 && label_perm_index[label] == -1) {
      label_perm_index[label] = perm_index++;
    }
  }

  // 对所有输入张量，同样补齐维度至与输出维度大小相同
  // 最后对输入做 permute 操作，使得输入张量的每一维
  // 与输出张量的每一维能对上
  std::vector<Tensor> permuted_operands;
  for (const auto i: c10::irange(num_ops)) {
    // 保存输入张量最终做 permute 时候的维度映射
    std::vector<int64_t> perm_shape(perm_index, -1);
    Tensor operand = operands[i];
    // 取输入张量对应的 equation
    const auto labels = op_labels[i];
    std::size_t j = 0;
    for (const auto& label : labels) {
      // ......
      // 建立输出张量等式字符索引到当前
      // 输入张量等式字符索引的映射
      perm_shape[label_perm_index[label]] = j++;
    }
    // 如果输入张量的维度小于补全后的输出
    // 那么 perm_shape 中一定存在值为 -1 的元素
    // 那么相当于需要扩充输入张量的维度
    // 扩充的维度添加在张量的尾部 
    for (int64_t& index : perm_shape) {
      if (index == -1) {
        // 在张量尾部插入维度1
        operand = operand.unsqueeze(-1);
        index = j++;
      }
    }
    // 把输入张量的维度按照输出张量的维度重排，采用 permute 操作
    permuted_operands.push_back(operand.permute(perm_shape)); 
  }
  // ......
  Tensor result = permuted_operands[0];
  // .....
  // 计算最终结果
  for (const auto i: c10::irange(1, num_ops)) {
    Tensor operand = permuted_operands[i];
    // 新建 vector 保存需要被累加的维度
    std::vector<int64_t> sum_dims;
    // ......
    // 最后的代码可以见 Pytorch 源码
    // 我还没有完全理解其实现，里面用的是 bmm 
    // 不过我觉得可以简单理解为将两个张量做广播
    // 然后根据累加维度做累加
    result = sumproduct_pair(result, operand, sum_dims, false);
  }

  return result;
}
