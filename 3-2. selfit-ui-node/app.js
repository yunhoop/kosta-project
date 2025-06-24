// app.js
require('dotenv').config();
const express = require('express');
const path = require('path');

const app = express();

// public/ 폴더 안의 정적 자원 서빙
app.use(express.static(path.join(__dirname, 'public')));

// SPA 용: 정적 파일에 매핑되지 않는 모든 경로를 메인 HTML로 포워딩
app.get('*', (req, res) => {
  res.sendFile(path.join(__dirname, 'public/html/index.html'));
});

// bin/www 에서 이 app을 불러다 쓰도록 export
module.exports = app;
