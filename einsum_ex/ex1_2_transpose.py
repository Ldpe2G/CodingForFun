import torch
import numpy as np

a = torch.arange(6).reshape(2, 3)
# i = 2, j = 3
torch_ein_out = torch.einsum('ij->ji', [a]).numpy()
torch_org_out = torch.transpose(a, 0, 1).numpy()

np_a = a.numpy()
# numpy implementation
np_out = np.empty((3, 2), dtype=np.int32)
# outer loop for free indices
for j in range(0, 3):
    for i in range(0, 2):
        # inner loop for summing indices
        # for transpose example there is no inner sum loop
        sum_result = 0
        for inner in range(0, 1):
            sum_result += np_a[i, j]
        np_out[j, i] = sum_result

print("input:\n", np_a)
print("torch ein out: \n", torch_ein_out)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_org_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_ein_out == torch_org_out ?", np.allclose(torch_ein_out, torch_org_out))

# input:
#  [[0 1 2]
#  [3 4 5]]
# torch ein out:
#  [[0 3]
#  [1 4]
#  [2 5]]
# torch org out:
#  [[0 3]
#  [1 4]
#  [2 5]]
# numpy out:
#  [[0 3]
#  [1 4]
#  [2 5]]
# is np_out == torch_org_out ? True
# is torch_ein_out == torch_org_out ? True
