import torch
import numpy as np

a = torch.randn(2,3,5,7,9)
# i = 7, j = 9
torch_ein_out = torch.einsum('...ij->...ji', [a]).numpy()
torch_org_out = a.permute(0, 1, 2, 4, 3).numpy()

np_a = a.numpy()
# numpy implementation
np_out = np.empty((2,3,5,9,7), dtype=np.float32)
# outer loop for free indices
for j in range(0, 9):
    for i in range(0, 7):
        # inner loop for summing indices
        # for this example there is no inner sum loop
        sum_result = 0
        for inner in range(0, 1):
            sum_result += np_a[..., i, j]
        np_out[..., j, i] = sum_result

print("is np_out == torch_org_out ?", np.allclose(torch_ein_out, np_out))
print("is torch_ein_out == torch_org_out ?", np.allclose(torch_ein_out, torch_org_out))

# is np_out == torch_org_out ? True
# is torch_ein_out == torch_org_out ? True
