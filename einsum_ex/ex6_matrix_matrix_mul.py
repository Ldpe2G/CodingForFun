import torch
import numpy as np

a = torch.arange(6).reshape(2, 3)
b = torch.arange(15).reshape(3, 5)
# i = 2, k = 3, j = 5
torch_ein_out = torch.einsum('ik,kj->ij', [a, b]).numpy()
torch_ein_out2 = torch.einsum('ik,kj', [a, b]).numpy() # equal form
torch_org_out = torch.mm(a, b).numpy()

np_a = a.numpy()
np_b = b.numpy()
# numpy implementation
np_out = np.empty((2, 5), dtype=np.int32)
# outer loop for free indices
for i in range(0, 2):
    for j in range(0, 5):
        # inner loop for multiplication and summing indices
        sum_result = 0
        for k in range(0, 3):
            sum_result += np_a[i, k] * np_b[k, j]
        np_out[i, j] = sum_result

print("matrix a:\n", np_a)
print("matrix b:\n", np_b)
print("torch ein out: \n", torch_ein_out)
print("torch ein out2: \n", torch_ein_out2)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is numpy == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_ein_out2 == torch_ein_out ?", np.allclose(torch_ein_out2, torch_ein_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_org_out, torch_ein_out))

# matrix a:
#  [[0 1 2]
#  [3 4 5]]
# matrix b:
#  [[ 0  1  2  3  4]
#  [ 5  6  7  8  9]
#  [10 11 12 13 14]]
# torch ein out:
#  [[ 25  28  31  34  37]
#  [ 70  82  94 106 118]]
# torch ein out2:
#  [[ 25  28  31  34  37]
#  [ 70  82  94 106 118]]
# torch org out:
#  [[ 25  28  31  34  37]
#  [ 70  82  94 106 118]]
# numpy out:
#  [[ 25  28  31  34  37]
#  [ 70  82  94 106 118]]
# is numpy == torch_ein_out ? True
# is torch_ein_out2 == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
