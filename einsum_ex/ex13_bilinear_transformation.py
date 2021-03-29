import torch
import numpy as np

a = torch.randn(2,3)
b = torch.randn(5,3,7)
c = torch.randn(2,7)
# i = 2, k = 3, j = 5, l = 7
torch_ein_out = torch.einsum('ik,jkl,il->ij', [a, b, c]).numpy()
m = torch.nn.Bilinear(3, 7, 5, bias=False)
m.weight.data = b
torch_org_out = m(a, c).detach().numpy()

np_a = a.numpy()
np_b = b.numpy()
np_c = c.numpy()
# numpy implementation
np_out = np.empty((2, 5), dtype=np.float32)
# outer loop for free indices
for i in range(0, 2):
    for j in range(0, 5):
        # inner loop for multiplication and summing indices
        sum_result = 0
        for k in range(0, 3):
            for l in range(0, 7):
                sum_result += np_a[i, k] * np_b[j, k, l] * np_c[i, l]
        np_out[i, j] = sum_result

# print("matrix a:\n", np_a)
# print("matrix b:\n", np_b)
print("torch ein out: \n", torch_ein_out)
print("torch org out: \n", torch_org_out)
print("numpy out: \n", np_out)
print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_org_out == torch_ein_out ?", np.allclose(torch_ein_out, torch_org_out))


# torch ein out:
#  [[-2.9185116   0.17024004 -0.43915534  1.5860008  10.016678  ]
#  [-0.48688257 -3.5114982  -0.7543343  -0.46790922  1.4816089 ]]
# torch org out:
#  [[-2.9185116   0.17024004 -0.43915534  1.5860008  10.016678  ]
#  [-0.48688257 -3.5114982  -0.7543343  -0.46790922  1.4816089 ]]
# numpy out:
#  [[-2.9185114   0.17023998 -0.4391551   1.5860008  10.016678  ]
#  [-0.4868826  -3.5114982  -0.7543342  -0.4679092   1.4816089 ]]
# is np_out == torch_ein_out ? True
# is torch_org_out == torch_ein_out ? True
