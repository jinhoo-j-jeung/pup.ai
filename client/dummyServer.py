from flask import Flask
from flask import request
from flask import jsonify
import sys

app = Flask(__name__)

@app.route('/user/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get("email")
    password = data.get("password")

    if username == 'test@test.com' and password == 'test':
        return jsonify({'token': 'Token123'}), 200
    else:
        return "", 400
@app.route('/user/register', methods=['POST'])
def signup():
    data = request.get_json()
    username = data.get("email")
    password = data.get("password")

    if username != 'shepi13@gmail.com':
        return jsonify({'token': 'Token123'}), 200
    else:
        return "", 400

@app.route('/breed', methods=['POST'])
def index():
    return "running"

@app.route('/user/profile', methods=['GET'])
def profile():
    return "running"

@app.route('/send_photo', methods=['POST'])
def send_photo():
    data = request.get_data()
    with open('upload.png', 'wb') as new_file:
        new_file.write(data)
        
    return 'Success'


if __name__ == '__main__':
    app.run(use_reloader=False, port=8000)
