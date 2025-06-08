import React from 'react';
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  Legend,
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  CartesianGrid,
  ResponsiveContainer,
} from 'recharts';

const COLORS = ['#8884d8', '#00C49F', '#FFBB28', '#FF8042', '#AA66CC', '#FF4444'];
const TOTAL_MEMORY_GB = 15;

const CustomTooltip = ({ active, payload }) => {
  if (!active || !payload || payload.length === 0) return null;
  return (
    <div style={{ background: '#333', color: '#fff', padding: '8px', borderRadius: '5px' }}>
      {payload.map((entry, i) => (
        <div key={i}>
          <strong>{entry.name}</strong>: {Number(entry.value).toFixed(2)}
        </div>
      ))}
    </div>
  );
};

const formatTimestamp = (timestamp) => {
  const date = new Date(timestamp);
  const hours = date.getHours();
  return `${hours}:00`;
};

const ChartDisplay = ({ data, title, height }) => {
  if (!data || data.length === 0) return <p className="text-light">No data to display</p>;

  const isTimeSeries = data.some((item) => item.timestamp !== null);
  const isMemory = title.includes('Memory');
  const isDisk = title.includes('Disk');

  const processedData = data.map((item) => {
    let label = item.label
      .replace(/^Core /, '')         // Remove "Core " prefix
      .replace('/', 'E:')
      .replace('/boot/efi', 'F:');

    if (label === 'E:') label = 'E:';
    if (label === 'F:') label = 'F:';

    // Convert memory % to GB
    let value = isMemory ? (item.value * TOTAL_MEMORY_GB) / 100 : item.value;

    return {
      ...item,
      label,
      value: Number(value.toFixed(2))
    };
  });

  const uniqueLabels = [...new Set(processedData.map((item) => item.label))];
  const isMultipleLabels = uniqueLabels.length > 1;

  let formattedData = isTimeSeries
    ? Object.values(
        processedData.reduce((acc, item) => {
          const key = item.timestamp;
          if (!acc[key]) acc[key] = { timestamp: key };
          acc[key][item.label] = item.value;
          return acc;
        }, {})
      )
    : processedData;

  // Fallback for single-point line chart
  if (isTimeSeries && formattedData.length === 1) {
    const cloned = { ...formattedData[0] };
    cloned.timestamp = `${formattedData[0].timestamp}-copy`;
    formattedData.push(cloned);
  }

  // Sort timestamps numerically
  if (isTimeSeries) {
    formattedData = formattedData.sort((a, b) => {
      const aTime = a.timestamp.match(/\d+/g)?.join('');
      const bTime = b.timestamp.match(/\d+/g)?.join('');
      return Number(aTime) - Number(bTime);
    });
  }

  return (
    <ResponsiveContainer width="100%" height={height}>
      {isMemory ? (
        <PieChart>
          <Pie
            data={formattedData}
            dataKey="value"
            nameKey="label"
            cx="50%"
            cy="50%"
            outerRadius={100}
            fill="#8884d8"
            label={({ name, percent }) => `${name} ${(percent * 100).toFixed(1)}%`}
          >
            {formattedData.map((entry, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Legend verticalAlign="bottom" />
        </PieChart>
      ) : isDisk ? (
        <BarChart
          data={processedData}
          margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="label" />
          <YAxis />
          <Tooltip content={<CustomTooltip />} />
          <Legend />
          <Bar dataKey="value" name="Disk Usage (%)" isAnimationActive={false}>
            {processedData.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={COLORS[index % COLORS.length]}
              />
            ))}
          </Bar>
        </BarChart>
      ) : isTimeSeries ? (
        <LineChart
          data={formattedData}
          margin={{ top: 20, right: 30, left: 0, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="timestamp" tickFormatter={formatTimestamp} />
          <YAxis />
          <Tooltip content={<CustomTooltip />} />
          <Legend />
          {uniqueLabels.map((label, index) => (
            <Line
              key={label}
              type="monotone"
              dataKey={label}
              stroke={COLORS[index % COLORS.length]}
              dot={{ r: 3 }}
              strokeWidth={2}
              connectNulls={true}
              isAnimationActive={false}
            />
          ))}
        </LineChart>
      ) : (
        <BarChart
          data={formattedData}
          layout="vertical"
          margin={{ top: 20, right: 30, left: 60, bottom: 5 }}
        >
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis type="number" />
          <YAxis type="category" dataKey="label" />
          <Tooltip content={<CustomTooltip />} />
          <Legend />
          <Bar dataKey="value" fill="#8884d8" barSize={20} />
        </BarChart>
      )}
    </ResponsiveContainer>
  );
};

export default ChartDisplay;
