import torch
import numpy as np

a = torch.arange(3)
b = torch.arange(3,7)  # -- a vector of length 4 containing [3, 4, 5, 6]
# i = 3, j = 4
torch_ein_out = torch.einsum('i,j->ij', [a, b]).numpy()
torch_ein_out2 = torch.einsum('i,j', [a, b]).numpy() # equal form
torch_org_out = torch.outer(a, b).numpy()

np_a = a.numpy()
np_b = b.numpy() 
# numpy implementation
np_out = np.empty((3, 4), dtype=np.int32)
# outer loop for free indices
for i in range(0, 3):
    for j in range(0, 4):
        # inner loop for multiplication and summing indices
        # for this example, there is no summing indices
        sum_result = 0
        for inner in range(0, 1):
            sum_result += np_a[i] * np_b[j]
        np_out[i, j] = sum_result

print("vector a:\n", np_a)
print("vector b:\n", np_b)
print("torch ein out: \n", torch_ein_out)
print("torch ein out2: \n", torch_ein_out2)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_ein_out2 == torch_ein_out ?", np.allclose(torch_ein_out2, torch_ein_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_org_out, torch_ein_out))


# vector a:
#  [0 1 2]
# vector b:
#  [3 4 5 6]
# torch ein out:
#  [[ 0  0  0  0]
#  [ 3  4  5  6]
#  [ 6  8 10 12]]
# torch ein out2:
#  [[ 0  0  0  0]
#  [ 3  4  5  6]
#  [ 6  8 10 12]]
# torch org out:
#  [[ 0  0  0  0]
#  [ 3  4  5  6]
#  [ 6  8 10 12]]
# numpy out:
#  [[ 0  0  0  0]
#  [ 3  4  5  6]
#  [ 6  8 10 12]]
# is np_out == torch_ein_out ? True
# is torch_ein_out2 == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
