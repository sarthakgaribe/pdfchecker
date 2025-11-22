/**
 * Formatting utility functions
 */

/**
 * Format file size to human-readable format
 */
export const formatFileSize = (bytes) => {
    if (bytes === 0) return '0 Bytes';
    
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i];
  };
  
  /**
   * Format timestamp to readable date
   */
  export const formatTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };
  
  /**
   * Format processing time
   */
  export const formatProcessingTime = (milliseconds) => {
    if (milliseconds < 1000) {
      return `${milliseconds}ms`;
    }
    return `${(milliseconds / 1000).toFixed(2)}s`;
  };
  
  /**
   * Get status class name
   */
  export const getStatusClass = (status) => {
    const classes = {
      PASS: 'status-pass',
      FAIL: 'status-fail',
      ERROR: 'status-error',
      ALL_PASS: 'status-pass',
      ALL_FAIL: 'status-fail',
      PARTIAL_PASS: 'status-warning',
    };
    
    return classes[status] || 'status-default';
  };
  
  /**
   * Get confidence level description
   */
  export const getConfidenceLevel = (confidence) => {
    if (confidence >= 90) return 'Very High';
    if (confidence >= 75) return 'High';
    if (confidence >= 60) return 'Medium';
    if (confidence >= 40) return 'Low';
    return 'Very Low';
  };