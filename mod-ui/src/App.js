import React, { useState } from 'react';
import axios from 'axios';
import './App.css';

function App() {
  const [originalUrl, setOriginalUrl] = useState('');
  const [shortenedUrl, setShortenedUrl] = useState('');
  const [shortCode, setShortCode] = useState('');
  const [analytics, setAnalytics] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const API_BASE = 'http://localhost:8080';
  const ANALYTICS_API_BASE = 'http://localhost:8081';

  const shortenUrl = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const response = await axios.post(`${API_BASE}/api/shorten`, {
        originalUrl: originalUrl
      });

      setShortenedUrl(response.data.shortUrl);
      setShortCode(response.data.shortCode);
    } catch (err) {
      setError('Failed to shorten URL. Please try again.');
      console.error('Error shortening URL:', err);
    } finally {
      setLoading(false);
    }
  };

  const getAnalytics = async () => {
    if (!shortCode) return;

    try {
      const response = await axios.get(`${ANALYTICS_API_BASE}/api/analytics/${shortCode}`);
      setAnalytics(response.data);
    } catch (err) {
      console.error('Error fetching analytics:', err);
    }
  };

  const copyToClipboard = () => {
    navigator.clipboard.writeText(shortenedUrl);
    alert('Copied to clipboard!');
  };

  return (
      <div className="App">
        <header className="App-header">
          <h1>🔗 URL Shortener</h1>
          <p>Shorten your long URLs quickly and easily</p>
        </header>

        <main className="main-content">
          <div className="shortener-form">
            <form onSubmit={shortenUrl}>
              <div className="input-group">
                <input
                    type="url"
                    value={originalUrl}
                    onChange={(e) => setOriginalUrl(e.target.value)}
                    placeholder="Enter your long URL here..."
                    required
                    className="url-input"
                />
                <button
                    type="submit"
                    disabled={loading}
                    className="shorten-btn"
                >
                  {loading ? 'Shortening...' : 'Shorten'}
                </button>
              </div>
            </form>

            {error && (
                <div className="error-message">
                  {error}
                </div>
            )}

            {shortenedUrl && (
                <div className="result-section">
                  <h3>Your shortened URL:</h3>
                  <div className="result-box">
                    <a href={shortenedUrl} target="_blank" rel="noopener noreferrer">
                      {shortenedUrl}
                    </a>
                    <button onClick={copyToClipboard} className="copy-btn">
                      Copy
                    </button>
                  </div>

                  <button onClick={getAnalytics} className="analytics-btn">
                    View Analytics
                  </button>
                </div>
            )}

            {analytics && (
                <div className="analytics-section">
                  <h3>Analytics for: {analytics.shortCode}</h3>
                  <div className="analytics-stats">
                    <div className="stat">
                      <span className="stat-number">{analytics.totalClicks}</span>
                      <span className="stat-label">Total Clicks</span>
                    </div>
                    <div className="stat">
                      <span className="stat-number">{analytics.todayClicks}</span>
                      <span className="stat-label">Today's Clicks</span>
                    </div>
                  </div>

                  {analytics.recentClicks && analytics.recentClicks.length > 0 && (
                      <div className="recent-clicks">
                        <h4>Recent Clicks:</h4>
                        <ul>
                          {analytics.recentClicks.map((click, index) => (
                              <li key={index}>
                                {new Date(click.clickedAt).toLocaleString()} - {click.ipAddress}
                              </li>
                          ))}
                        </ul>
                      </div>
                  )}
                </div>
            )}
          </div>
        </main>

        <footer className="footer">
          <p>Built with ❤️ for DevOps learning</p>
        </footer>
      </div>
  );
}

export default App;