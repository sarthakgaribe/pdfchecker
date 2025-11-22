/**
 * Main application component
 */
import React, { useState } from 'react';
import { checkPdfDocument } from './services/api.js';
import { validateForm } from './utils/validators.js';
import { 
  formatFileSize, 
  formatTimestamp, 
  formatProcessingTime,
  getStatusClass,
  getConfidenceLevel 
} from './utils/formatters.js';
import { DEFAULT_RULES } from './constants/validationRules.js';

function App() {
  const [file, setFile] = useState(null);
  const [rules, setRules] = useState(DEFAULT_RULES);
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);

  /**
   * Handle file selection
   */
  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    if (selectedFile) {
      setFile(selectedFile);
      setError(null);
    }
  };

  /**
   * Handle rule change
   */
  const handleRuleChange = (index, value) => {
    const newRules = [...rules];
    newRules[index] = value;
    setRules(newRules);
  };

  /**
   * Add new rule
   */
  const addRule = () => {
    if (rules.length < 10) {
      setRules([...rules, '']);
    }
  };

  /**
   * Remove rule
   */
  const removeRule = (index) => {
    if (rules.length > 1) {
      const newRules = rules.filter((_, i) => i !== index);
      setRules(newRules);
    }
  };

  /**
   * Handle form submission
   */
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Filter out empty rules
    const validRules = rules.filter(rule => rule.trim() !== '');
    
    // Validate form
    const validation = validateForm(file, validRules);
    if (!validation.isValid) {
      setError({
        message: 'Validation failed',
        errors: validation.errors,
      });
      return;
    }

    setLoading(true);
    setError(null);
    setResult(null);

    try {
      const response = await checkPdfDocument(file, validRules);
      setResult(response);
    } catch (err) {
      setError({
        message: err.message || 'Failed to check PDF',
        details: err.details,
        errors: err.errors || [],
      });
    } finally {
      setLoading(false);
    }
  };

  /**
   * Reset form
   */
  const handleReset = () => {
    setFile(null);
    setRules(DEFAULT_RULES);
    setResult(null);
    setError(null);
  };

  return (
    <div className="app">
      <div className="container">
        {/* Header */}
        <header className="header">
          <h1>PDF Compliance Checker</h1>
          <p>Upload a PDF and define rules to verify document compliance</p>
        </header>

        {/* Form Section */}
        <div className="card">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              {/* File Upload */}
              <div className="form-section">
                <label className="label">Upload PDF Document</label>
                {!file ? (
                  <div className="file-upload-area">
                    <input
                      type="file"
                      accept=".pdf"
                      onChange={handleFileChange}
                      className="file-input"
                      id="file-upload"
                    />
                    <label htmlFor="file-upload" className="file-upload-label">
                      <div className="upload-icon">📄</div>
                      <div>Click to upload or drag and drop</div>
                      <div className="upload-hint">PDF files only (Max 10MB)</div>
                    </label>
                  </div>
                ) : (
                  <div className="file-selected">
                    <div className="file-info">
                      <div className="file-icon">📄</div>
                      <div>
                        <div className="file-name">{file.name}</div>
                        <div className="file-size">{formatFileSize(file.size)}</div>
                      </div>
                    </div>
                    <button
                      type="button"
                      onClick={() => setFile(null)}
                      className="btn-remove"
                    >
                      ✕
                    </button>
                  </div>
                )}
              </div>

              {/* Rules Input */}
              <div className="form-section">
                <div className="rules-header">
                  <label className="label">Compliance Rules</label>
                  <button
                    type="button"
                    onClick={addRule}
                    disabled={rules.length >= 10}
                    className="btn-add-rule"
                  >
                    + Add Rule
                  </button>
                </div>
                <div className="rules-list">
                  {rules.map((rule, index) => (
                    <div key={index} className="rule-item">
                      <div className="rule-input-wrapper">
                        <label className="rule-label">Rule {index + 1}</label>
                        <input
                          type="text"
                          value={rule}
                          onChange={(e) => handleRuleChange(index, e.target.value)}
                          placeholder="e.g., The document must mention a date"
                          className="input"
                        />
                      </div>
                      {rules.length > 1 && (
                        <button
                          type="button"
                          onClick={() => removeRule(index)}
                          className="btn-remove-rule"
                        >
                          🗑️
                        </button>
                      )}
                    </div>
                  ))}
                </div>
                <div className="hint">Enter 1-10 rules to check against the document</div>
              </div>
            </div>

            {/* Error Display */}
            {error && (
              <div className="error-box">
                <div className="error-icon">⚠️</div>
                <div>
                  <div className="error-title">{error.message}</div>
                  {error.details && <div className="error-details">{error.details}</div>}
                  {error.errors && error.errors.length > 0 && (
                    <ul className="error-list">
                      {error.errors.map((err, index) => (
                        <li key={index}>{err}</li>
                      ))}
                    </ul>
                  )}
                </div>
              </div>
            )}

            {/* Action Buttons */}
            <div className="button-group">
              <button
                type="submit"
                disabled={!file || loading}
                className="btn btn-primary"
              >
                {loading ? 'Checking...' : 'Check Document'}
              </button>
              {(file || result) && (
                <button
                  type="button"
                  onClick={handleReset}
                  disabled={loading}
                  className="btn btn-secondary"
                >
                  Reset
                </button>
              )}
            </div>
          </form>
        </div>

        {/* Loading State */}
        {loading && (
          <div className="card">
            <div className="loading">
              <div className="spinner"></div>
              <div className="loading-text">Analyzing document with AI...</div>
              <div className="loading-hint">This may take a few moments</div>
            </div>
          </div>
        )}

        {/* Results */}
        {!loading && result && (
          <div className="results">
            {/* Summary Card */}
            <div className="card">
              <h2>Check Results</h2>
              <div className="summary-grid">
                <div className="summary-item">
                  <div className="summary-label">File Name</div>
                  <div className="summary-value">{result.fileName}</div>
                </div>
                <div className="summary-item">
                  <div className="summary-label">Total Pages</div>
                  <div className="summary-value">{result.totalPages}</div>
                </div>
                <div className="summary-item">
                  <div className="summary-label">Overall Status</div>
                  <span className={`status-badge ${getStatusClass(result.overallStatus)}`}>
                    {result.overallStatus.replace('_', ' ')}
                  </span>
                </div>
                <div className="summary-item">
                  <div className="summary-label">Processing Time</div>
                  <div className="summary-value">
                    {formatProcessingTime(result.processingTimeMs)}
                  </div>
                </div>
              </div>
            </div>

            {/* Results Table */}
            <div className="card">
              <div className="table-container">
                <table className="results-table">
                  <thead>
                    <tr>
                      <th>Rule</th>
                      <th>Status</th>
                      <th>Evidence</th>
                      <th>Reasoning</th>
                      <th>Confidence</th>
                    </tr>
                  </thead>
                  <tbody>
                    {result.results.map((item, index) => (
                      <tr key={index}>
                        <td>{item.rule}</td>
                        <td>
                          <span className={`status-badge ${getStatusClass(item.status)}`}>
                            {item.status}
                          </span>
                        </td>
                        <td>{item.evidence}</td>
                        <td>{item.reasoning}</td>
                        <td>
                          <div className="confidence">
                            <div className="confidence-value">{item.confidence}%</div>
                            <div className="confidence-level">
                              {getConfidenceLevel(item.confidence)}
                            </div>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Timestamp */}
            <div className="timestamp">
              Checked on {formatTimestamp(result.timestamp)}
            </div>
          </div>
        )}

        {/* Footer */}
        <footer className="footer">
          <p>Powered by Spring Boot & React | Built with ❤️ for NIYAMR AI</p>
        </footer>
      </div>
    </div>
  );
}

export default App;