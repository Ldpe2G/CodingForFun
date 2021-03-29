import torch
import numpy as np

a = torch.arange(3)
b = torch.arange(3, 6) # vector of length 3 containing [3, 4, 5]
# i = 3
torch_ein_out = torch.einsum('i,i->', [a, b]).numpy()
torch_ein_out2 = torch.einsum('i,i', [a, b]).numpy() # equal form
torch_org_out = torch.dot(a, b).numpy()

np_a = a.numpy()
np_b = b.numpy() 
# numpy implementation
np_out = np.empty((1, ), dtype=np.int32)
# outer loop for free indices
# for this example, there is no free indices
for o in range(0, 1):
    # inner loop for multiplication and summing indices
    sum_result = 0
    for i in range(0, 3):
        sum_result += np_a[i] * np_b[i]
    np_out[o] = sum_result

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
#  [3 4 5]
# torch ein out:
#  14
# torch ein out2:
#  14
# torch org out:
#  14
# numpy out:
#  [14]
# is np_out == torch_ein_out ? True
# is torch_ein_out2 == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
