// app.js

const express = require('express');
const path    = require('path');
const app     = express();

// public/ 폴더 안의 정적 자원 서빙
app.use(express.static(path.join(__dirname, 'public')));

// SPA 진입점으로 매핑되지 않은 모든 경로를 spa.html로 포워딩
app.get('*', (req, res) => {
  res.sendFile(
      path.join(__dirname, 'public', 'html', 'spa.html'),
      err => {
        if (err) {
          console.error('spa.html 전송 실패:', err);
          res.status(500).send('SPA 로드 중 오류 발생');
        }
      }
  );
});

module.exports = app;
