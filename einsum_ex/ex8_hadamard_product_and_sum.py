import torch
import numpy as np

a = torch.arange(6).reshape(2, 3)
b = torch.arange(6,12).reshape(2, 3)
# i = 2, j = 3
torch_ein_out = torch.einsum('ij,ij->', [a, b]).numpy()
torch_ein_out2 = torch.einsum('ij,ij', [a, b]).numpy() # equal form
torch_org_out = (a * b).sum().numpy()

np_a = a.numpy()
np_b = b.numpy() 
# numpy implementation
np_out = np.empty((1, ), dtype=np.int32)
# outer loop for free indices
# for this example, there is no free indices
for o in range(0, 1):
    # inner loop for multiplication and summing indices
    sum_result = 0
    for i in range(0, 2):
        for j in range(0, 3):
            sum_result += np_a[i,j] * np_b[i,j]
    np_out[o] = sum_result

print("matrix a:\n", np_a)
print("matrix b:\n", np_b)
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
# matrix b:
#  [[ 6  7  8]
#  [ 9 10 11]]
# torch ein out:
#  145
# torch ein out2:
#  145
# torch org out:
#  145
# numpy out:
#  [145]
# is np_out == torch_ein_out ? True
# is torch_ein_out2 == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
