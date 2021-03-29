import torch
import numpy as np

a = torch.randn(2,3,5,7)
b = torch.randn(11,13,3,17,5)
# p = 2, q = 3, r = 5, s = 7
# t = 11, u = 13, v = 17, r = 5
torch_ein_out = torch.einsum('pqrs,tuqvr->pstuv', [a, b]).numpy()
torch_org_out = torch.tensordot(a, b, dims=([1, 2], [2, 4])).numpy()

np_a = a.numpy()
np_b = b.numpy()
# numpy implementation
np_out = np.empty((2, 7, 11, 13, 17), dtype=np.float32)
# outer loop for free indices
for p in range(0, 2):
    for s in range(0, 7):
        for t in range(0, 11):
            for u in range(0, 13):
                for v in range(0, 17):

                    # inner loop for multiplication and summing indices
                    sum_result = 0
                    for q in range(0, 3):
                        for r in range(0, 5):
                            sum_result += np_a[p, q, r, s] * np_b[t, u, q, v, r]
                    np_out[p, s, t, u, v] = sum_result

print("is np_out == torch_ein_out ?", np.allclose(torch_ein_out, np_out, atol=1e-6))
print("is torch_ein_out == torch_org_out ?", np.allclose(torch_ein_out, torch_org_out, atol=1e-6))

# is np_out == torch_ein_out ? True
# is torch_ein_out == torch_org_out ? True
