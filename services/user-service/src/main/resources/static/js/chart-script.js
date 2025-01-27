document.getElementById('fetchData').addEventListener('click', () => {
  const symbolSelect = document.getElementById('symbolSelect');
  const selectedSymbols = Array.from(symbolSelect.selectedOptions).map(option => option.value);
  const period = document.getElementById('periodSelect').value;

  if (selectedSymbols.length === 0) {
      alert("Please select at least one symbol.");
      return;
  }

  const symbolsParam = selectedSymbols.join(",");
  let apiUrl = `/api/prices?symbols=${symbolsParam}`;
  if (period) {
      apiUrl += `&period=${period}`;
  }

  fetch(apiUrl)
      .then(response => response.json())
      .then(data => {
          const chartData = {};
          data.forEach(item => {
              if (!chartData[item.symbol]) {
                  chartData[item.symbol] = {
                      timestamps: [],
                      prices: []
                  };
              }
              chartData[item.symbol].timestamps.push(new Date(item.timestamp));
              chartData[item.symbol].prices.push(item.price);
          });

          const datasets = Object.keys(chartData).map(symbol => ({
              label: symbol,
              data: chartData[symbol].prices,
              borderColor: getRandomColor(), // Function to generate random colors
              borderWidth: 1,
              tension: 0.4
          }));

          const ctx = document.getElementById('priceChart').getContext('2d');
          new Chart(ctx, {
              type: 'line',
              data: {
                  labels: chartData[Object.keys(chartData)[0]].timestamps,
                  datasets: datasets
              },
              options: {
                  scales: {
                      x: {
                          type: 'time',
                          time: {
                              unit: 'day',
                              displayFormats: {
                                  day: 'MMM DD'
                              }
                          },
                          ticks: {
                              source: 'data'
                          }
                      }
                  }
              }
          });
      });
});

function getRandomColor() {
  const letters = '0123456789ABCDEF';
  let color = '#';
  for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}