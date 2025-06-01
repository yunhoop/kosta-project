document.addEventListener("DOMContentLoaded", function () {
    var options = {
        series: [{
            name: '하루 섭취 kcal ',
            data: [1500, 1700, 0, 0, 1900, 1950, 2200, 1900, 1200, 1700, 1900, 1500, 1300, 1900, 1700, 2100, 1700, 1500]
        }],
        chart: {
            height: 350,
            type: 'line',
            fontFamily: 'Noto Sans KR, sans-serif',
            toolbar: {
                show: false
            }
        },
        colors: ['#22DFCD'],
        fill: {
            type: 'solid'
        },
        markers: {
            size: 4, // 점 크기
            shape: 'circle', // 모양 ('circle' 기본값)
            strokeWidth: 3,  // 테두리 두께
            colors: ['#ffffff'],
            strokeColors: '#000000', // 테두리 색 (배경과 대비되게)
            fillOpacity: 1    // 안쪽 채움 투명도
        },
        stroke: {
            width: 5,
            curve: 'smooth'
        },
        xaxis: {
            type: 'datetime',
            categories: ['1/11/2000', '2/11/2000', '3/11/2000', '4/11/2000', '5/11/2000', '6/11/2000', '7/11/2000', '8/11/2000', '9/11/2000', '10/11/2000', '11/11/2000', '12/11/2000', '12/12/2000', '12/13/2000', '12/14/2000', '12/15/2000', '12/16/2000', '12/19/2000'],
            tickAmount: 10,
            labels: {
                formatter: function (value, timestamp, opts) {
                    return opts.dateFormatter(new Date(timestamp), 'dd MMM')
                },
                style: {
                    fontSize: '16px',
                    fontFamily: 'Noto Sans KR, sans-serif'
                }
            }
        },
        yaxis: {
            labels: {
                style: {
                    fontSize: '16px',
                    fontFamily: 'Noto Sans KR, sans-serif'
                }
            }
        },
        title: {
            text: '식단 기록 그래프',
            align: 'left',
            fontFamily: '"Noto Sans KR", sans-serif',
            style: {
                fontSize: "24px"
            }
        }

    };

    var chart = new ApexCharts(document.querySelector("#chart"), options);
    chart.render();
});