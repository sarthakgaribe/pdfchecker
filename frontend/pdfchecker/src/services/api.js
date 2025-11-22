/**
 * Core API service with axios configuration
 */
import axios from 'axios';
import { API_ENDPOINTS, API_CONFIG } from '../constants/apiEndpoints.js';

/**
 * Create axios instance with default configuration
 */
const apiClient = axios.create({
  timeout: API_CONFIG.TIMEOUT,
  headers: API_CONFIG.HEADERS,
});

/**
 * Request interceptor
 */
apiClient.interceptors.request.use(
  (config) => {
    console.log(`[API Request] ${config.method.toUpperCase()} ${config.url}`);
    return config;
  },
  (error) => {
    console.error('[API Request Error]', error);
    return Promise.reject(error);
  }
);

/**
 * Response interceptor
 */
apiClient.interceptors.response.use(
  (response) => {
    console.log(`[API Response] ${response.status} ${response.config.url}`);
    return response;
  },
  (error) => {
    console.error('[API Response Error]', error.response || error);
    
    if (error.response) {
      // Server responded with error status
      const { status, data } = error.response;
      return Promise.reject({
        status,
        message: data.message || 'An error occurred',
        details: data.details || '',
        errors: data.errors || [],
      });
    } else if (error.request) {
      // Request made but no response received
      return Promise.reject({
        status: 0,
        message: 'No response from server. Please check your connection.',
        details: 'Network error',
      });
    } else {
      // Error setting up request
      return Promise.reject({
        status: 0,
        message: error.message || 'An unexpected error occurred',
        details: 'Request configuration error',
      });
    }
  }
);

/**
 * Check PDF document against rules
 */
export const checkPdfDocument = async (file, rules) => {
  const formData = new FormData();
  formData.append('file', file);
  
  // Append each rule separately
  rules.forEach(rule => {
    formData.append('rules', rule);
  });
  
  try {
    const response = await apiClient.post(API_ENDPOINTS.CHECK_PDF, formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
    
    return response.data;
  } catch (error) {
    throw error;
  }
};

/**
 * Health check
 */
export const checkHealth = async () => {
  try {
    const response = await apiClient.get(API_ENDPOINTS.HEALTH);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export default {
  checkPdfDocument,
  checkHealth,
};