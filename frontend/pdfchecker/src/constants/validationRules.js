/**
 * Validation rules and constants
 */

export const VALIDATION_RULES = {
    MAX_FILE_SIZE_MB: 10,
    MAX_FILE_SIZE_BYTES: 10 * 1024 * 1024,
    ALLOWED_FILE_EXTENSION: '.pdf',
    MIN_RULES: 1,
    MAX_RULES: 10,
    MAX_RULE_LENGTH: 500,
  };
  
  export const VALIDATION_MESSAGES = {
    FILE_REQUIRED: 'Please select a PDF file',
    INVALID_FILE_TYPE: 'Only PDF files are allowed',
    FILE_TOO_LARGE: `File size must not exceed ${VALIDATION_RULES.MAX_FILE_SIZE_MB}MB`,
    RULES_REQUIRED: 'Please enter at least one rule',
    RULE_EMPTY: 'Rule cannot be empty',
    RULE_TOO_LONG: `Rule must not exceed ${VALIDATION_RULES.MAX_RULE_LENGTH} characters`,
    TOO_MANY_RULES: `Maximum ${VALIDATION_RULES.MAX_RULES} rules allowed`,
  };
  
  export const DEFAULT_RULES = [
    'The document must have a purpose section',
    'The document must mention at least one date',
    'The document must define at least one term',
  ];