import torch
import numpy as np

a = torch.arange(6).reshape(2, 3)
b = torch.arange(3)
# i = 2, k = 3
torch_ein_out = torch.einsum('ik,k->i', [a, b]).numpy()
torch_ein_out2 = torch.einsum('ik,k', [a, b]).numpy() # equal form
torch_org_out = torch.mv(a, b).numpy()

np_a = a.numpy()
np_b = b.numpy()
# numpy implementation
np_out = np.empty((2, ), dtype=np.int32)
# outer loop for free indices
for i in range(0, 2):
    # inner loop for multiplication and summing indices
    sum_result = 0
    for k in range(0, 3):
        sum_result += np_a[i, k] * np_b[k]
    np_out[i] = sum_result

print("matrix a:\n", np_a)
print("vector b:\n", np_b)
print("torch ein out: \n", torch_ein_out)
print("torch ein out2: \n", torch_ein_out2)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_ein_out2 == torch_ein_out ?", np.allclose(torch_ein_out2, torch_ein_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_org_out, torch_ein_out))

# matrix a:
#  [[0 1 2]
#  [3 4 5]]
# vector b:
#  [0 1 2]
# torch ein out:
#  [ 5 14]
# torch ein out2:
#  [ 5 14]
# torch org out:
#  [ 5 14]
# numpy out:
#  [ 5 14]
# is np_out == torch_ein_out ? True
# is torch_ein_out2 == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
