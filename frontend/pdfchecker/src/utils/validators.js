/**
 * Validation utility functions
 */
import { VALIDATION_RULES, VALIDATION_MESSAGES } from '../constants/validationRules.js';

/**
 * Validate PDF file
 */
export const validateFile = (file) => {
  if (!file) {
    return { isValid: false, error: VALIDATION_MESSAGES.FILE_REQUIRED };
  }
  
  // Check file type
  const fileName = file.name.toLowerCase();
  if (!fileName.endsWith(VALIDATION_RULES.ALLOWED_FILE_EXTENSION)) {
    return { isValid: false, error: VALIDATION_MESSAGES.INVALID_FILE_TYPE };
  }
  
  // Check file size
  if (file.size > VALIDATION_RULES.MAX_FILE_SIZE_BYTES) {
    return { isValid: false, error: VALIDATION_MESSAGES.FILE_TOO_LARGE };
  }
  
  return { isValid: true, error: null };
};

/**
 * Validate rules array
 */
export const validateRules = (rules) => {
  const errors = [];
  
  if (!rules || rules.length === 0) {
    return { isValid: false, errors: [VALIDATION_MESSAGES.RULES_REQUIRED] };
  }
  
  if (rules.length > VALIDATION_RULES.MAX_RULES) {
    errors.push(VALIDATION_MESSAGES.TOO_MANY_RULES);
  }
  
  rules.forEach((rule, index) => {
    if (!rule || rule.trim() === '') {
      errors.push(`Rule ${index + 1}: ${VALIDATION_MESSAGES.RULE_EMPTY}`);
    } else if (rule.length > VALIDATION_RULES.MAX_RULE_LENGTH) {
      errors.push(`Rule ${index + 1}: ${VALIDATION_MESSAGES.RULE_TOO_LONG}`);
    }
  });
  
  return { isValid: errors.length === 0, errors };
};

/**
 * Validate entire form
 */
export const validateForm = (file, rules) => {
  const allErrors = [];
  
  const fileValidation = validateFile(file);
  if (!fileValidation.isValid) {
    allErrors.push(fileValidation.error);
  }
  
  const rulesValidation = validateRules(rules);
  if (!rulesValidation.isValid) {
    allErrors.push(...rulesValidation.errors);
  }
  
  return {
    isValid: allErrors.length === 0,
    errors: allErrors,
  };
};