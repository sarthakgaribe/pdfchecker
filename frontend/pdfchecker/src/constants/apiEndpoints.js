/**
 * API endpoint constants
 */

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

export const API_ENDPOINTS = {
  CHECK_PDF: `${API_BASE_URL}/v1/pdf/check`,
  HEALTH: `${API_BASE_URL}/v1/pdf/health`,
};

export const API_CONFIG = {
  TIMEOUT: 60000, // 60 seconds
  HEADERS: {
    'Content-Type': 'application/json',
  }
};