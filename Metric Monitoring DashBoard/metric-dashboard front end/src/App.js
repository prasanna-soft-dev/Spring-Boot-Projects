import React, { useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css'; // Custom styling
import ChartDisplay from './components/ChartDisplay';

function App() {
  const [averageType, setAverageType] = useState('');
  const [timeRange, setTimeRange] = useState('');
  const [componentType, setComponentType] = useState('');
  const [showChart, setShowChart] = useState(false);
  const [chartData, setChartData] = useState([]);
  const [chartTitle, setChartTitle] = useState('');

  const handleGenerate = async () => {
    let title = '';
    let url = '';
    let component = '';

    switch (componentType) {
      case 'CPU_CORE_OVERALL':
        component = 'OVERALL_CPU_CORE';
        title = 'Overall CPU Core Average';
        break;
      case 'CPU_CORE_CORES':
        component = 'INDIVIDUAL_CPU_CORES';
        title = 'CPU Core Time Series';
        break;
      case 'MEMORY':
        component = 'MEMORY';
        title = 'Memory Usage';
        break;
      case 'DISK':
        component = 'DISK';
        title = 'Disk Partition Usage';
        break;
      default:
        return;
    }

    try {
      let response;

      if (componentType === 'CPU_CORE_CORES') {
        const params = new URLSearchParams({ averageType, timeRange });
        url = `http://localhost:8080/api/metrics/core-timeseries?${params.toString()}`;
      } else {
        const params = new URLSearchParams({ averageType, timeRange, component });
        url = `http://localhost:8080/api/metrics/average?${params.toString()}`;
      }

      response = await fetch(url);

      if (!response.ok) throw new Error(`Server Error: ${response.status}`);

      const result = await response.json();
      if (!Array.isArray(result)) return;

      setChartData(result);
      setChartTitle(title);
      setShowChart(true);
    } catch (error) {
      console.error('Failed to fetch chart data', error);
    }
  };

  return (
    <div className="app-background">
      <div className="container mt-4 p-4 rounded shadow-sm bg-white" style={{ maxWidth: '850px' }}>
        <h4 className="text-center mb-4 text-primary fw-bold">Metric Monitoring Dashboard</h4>

        <div className="row g-2 mb-3">
          <div className="col-3">
            <select
              className="form-select form-select-sm"
              value={averageType}
              onChange={(e) => setAverageType(e.target.value)}
            >
              <option value="" hidden>Choose Avg Type</option>
              <option value="HOUR">Hour</option>
              <option value="DAY">Day</option>
            </select>
          </div>

          <div className="col-3">
            <select
              className="form-select form-select-sm"
              value={timeRange}
              onChange={(e) => setTimeRange(e.target.value)}
            >
              <option value="" hidden>Choose Time Range</option>
              <option value="TODAY">Today</option>
              <option value="YESTERDAY">Yesterday</option>
              <option value="LAST_WEEK">Last Week</option>
            </select>
          </div>

          <div className="col-6 d-flex">
            <select
              className="form-select form-select-sm me-2"
              value={componentType}
              onChange={(e) => setComponentType(e.target.value)}
            >
              <option value="" hidden>Choose Component</option>
              <option value="CPU_CORE_OVERALL">Overall CPU</option>
              <option value="CPU_CORE_CORES">CPU Cores</option>
              <option value="MEMORY">Memory</option>
              <option value="DISK">Disk</option>
            </select>
            <button
              className="btn btn-sm btn-success"
              disabled={!averageType || !timeRange || !componentType}
              onClick={handleGenerate}
            >
              Generate
            </button>
          </div>
        </div>

        {showChart && (
          <div className="text-center mt-4">
            <p className="mb-2 fw-semibold">
              {chartTitle} — {averageType}, {timeRange}
            </p>
            <ChartDisplay data={chartData} title={chartTitle} height={300} />
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
