import torch
import numpy as np

a = torch.arange(9).reshape(3, 3)
# i = 3
torch_ein_out = torch.einsum('ii->i', [a]).numpy()
torch_org_out = torch.diagonal(a, 0).numpy()

np_a = a.numpy()
# numpy implementation
np_out = np.empty((3,), dtype=np.int32)
# outer loop for free indices
for i in range(0, 3):
    # inner loop for summing indices
    # for transpose example there is no inner sum loop
    sum_result = 0
    for inner in range(0, 1):
        sum_result += np_a[i, i]
    np_out[i] = sum_result

print("input:\n", np_a)
print("torch ein out: \n", torch_ein_out)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_ein_out, torch_org_out))
