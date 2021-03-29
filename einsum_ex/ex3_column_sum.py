import torch
import numpy as np

a = torch.arange(6).reshape(2, 3)
# i = 2, j = 3
torch_ein_out = torch.einsum('ij->j', [a]).numpy()
torch_org_out = torch.sum(a, dim=0).numpy()

np_a = a.numpy()
# numpy implementation
np_out = np.empty((3, ), dtype=np.int32)
# outer loop for free indices
for j in range(0, 3):
    # inner loop for summing indices
    sum_result = 0
    for i in range(0, 2):
        sum_result += np_a[i, j]
    np_out[j] = sum_result

print("input:\n", np_a)
print("torch ein out: \n", torch_ein_out)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_org_out, np_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_org_out, torch_ein_out))

# input:
#  [[0 1 2]
#  [3 4 5]]
# torch ein out:
#  [3 5 7]
# torch org out:
#  [3 5 7]
# numpy out:
#  [3 5 7]
# is np_out == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
