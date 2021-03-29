import torch
import numpy as np

a = torch.randn(2,3,5)
b = torch.randn(2,5,4)
# i = 2, j = 3, k = 5, l = 4
torch_ein_out = torch.einsum('ijk,ikl->ijl', [a, b]).numpy()
torch_org_out = torch.bmm(a, b).numpy() 

np_a = a.numpy()
np_b = b.numpy()
# numpy implementation
np_out = np.empty((2, 3, 4), dtype=np.float32)
# outer loop for free indices
for i in range(0, 2):
    for j in range(0, 3):
        for l in range(0, 4):
            # inner loop for multiplication and summing indices
            sum_result = 0
            for k in range(0, 5):
                sum_result += np_a[i, j, k] * np_b[i, k, l]
            np_out[i, j, l] = sum_result

# print("matrix a:\n", np_a)
# print("matrix b:\n", np_b)
# print("torch ein out: \n", torch_ein_out)
# print("torch org out: \n", torch_org_out)
# print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_ein_out, torch_org_out))

# is np_out == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
