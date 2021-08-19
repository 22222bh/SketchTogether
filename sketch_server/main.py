from flask import Flask, render_template, request
from datetime import datetime as dt
from KNN import KNN
from PIL import Image



app = Flask(__name__)

@app.route('/')
def time_page():
    time_str = str(dt.now().time())
    return "Time: " + time_str

@app.route('/model')
def run_model(): # 이미지를 전달해야 함
    knn = KNN() # 이거를 1회만
    res = knn.predict("a")
    return str(res)

@app.route('/test', methods=['POST'])
def run_model_test():
    image = request.files['files']
    img = Image.open(image)
    knn = KNN() # 이거를 1회만
    res = knn.predict(img)
    return str(res)

if __name__ == '__main__':
    app.run(host="0.0.0.0", port=4567, debug=False)
    # app.run(port=1112)