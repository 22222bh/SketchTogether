# 모듈화를 해서 세팅 단계는 한 번만 호출하도록 하고, feature를 append 하는 부분을 반복하도록.

class KNN:
    def __init__(self):
        from efficientnet_pytorch import EfficientNet
        from sklearn.cluster import KMeans
        from sklearn.decomposition import PCA
        import logging
        import torch
        import pickle
        from tqdm import tqdm
        from torchvision import transforms
        import numpy as np
        
        import os
        from sklearn.manifold import TSNE
        from torch import nn
        
        # import matplotlib.pyplot as plt # 테스트 용
        # 테스트를 위해서 matplot과 데이터셋 이미지들을 서버에 포함시켰음. 배포시 필요 x

        self.base_path = "/home/blee/sketch_server/"
        feat_path = os.path.join(self.base_path, "data/features_0817.npy")
        filename_path = os.path.join(self.base_path, "data/filenames_0817.npy")

        self.tfms = transforms.Compose([transforms.Resize(224), transforms.ToTensor(),
            transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225]),])
        self.model = EfficientNet.from_pretrained('efficientnet-b0')
        
        # Load feature vector
        self.feat_original = np.load(feat_path)
        self.filename = np.load(filename_path)
        self.tsne = TSNE(n_components=2, init='pca', random_state=0)
        self.avg_pooling = nn.AdaptiveAvgPool2d(1)


    def predict(self, image):
        from PIL import Image
        from sklearn.neighbors import NearestNeighbors
        import numpy as np
        def feature_extraction(image, model):
            img = self.tfms(image.convert("RGB")).unsqueeze(0)
            features = model.extract_features(img)
            return features

        new_feat = feature_extraction(image, self.model)
        new_feat = self.avg_pooling(new_feat)
        new_feat = new_feat.detach().numpy().reshape(-1)
        new_feat = new_feat.reshape(1, -1)
        feat = self.feat_original
        feat = np.append(feat, new_feat, axis = 0)

        # Reduce dimension
        feat = self.tsne.fit_transform(feat)
        knn = NearestNeighbors(n_neighbors=6, algorithm='auto').fit(feat)
        distances, indices = knn.kneighbors([list(feat[-1])])
        indices = indices[0][1:]
        res = []
        for idx in indices:
            res.append(self.filename[idx])
        return res
# image = []
# name = []
# for idx in indices:
#     res.append(filename[idx])
#     image_path = base_path + "imageset/" + filename[idx].split('_')[0] + "/" + filename[idx] + ".jpg"
#     image.append(np.array(Image.open(image_path)))
#     name.append(filename[idx])

# for i, img in enumerate(image):
#     plt.subplot(1,6,i+1)
#     plt.title(name[i])
#     plt.imshow(img, 'gray')

# plt.tight_layout()
# plt.show()