import * as Sentry from '@sentry/react';

export function SentryTestButton() {
  const testSentry = () => {
    console.log('🧪 Testing Sentry...');

    // Test 1: Capture exception
    Sentry.captureException(new Error('Test error from SentryTestButton - ' + Date.now()));

    // Test 2: Capture message
    Sentry.captureMessage('Test message from SentryTestButton - ' + Date.now(), 'error');

    console.log('✅ Sent test error and message to Sentry. Check your dashboard!');
    alert('Test events sent! Check Sentry dashboard in ~10 seconds.');
  };

  const testUnhandledError = () => {
    console.log('🧪 Throwing unhandled error...');
    setTimeout(() => {
      throw new Error('Unhandled test error - ' + Date.now());
    }, 100);
  };

  return (
    <div style={{
      position: 'fixed',
      bottom: '20px',
      right: '20px',
      zIndex: 9999,
      display: 'flex',
      flexDirection: 'column',
      gap: '10px',
    }}>
      <button
        onClick={testSentry}
        style={{
          padding: '12px 24px',
          backgroundColor: '#e74c3c',
          color: 'white',
          border: 'none',
          borderRadius: '6px',
          cursor: 'pointer',
          fontWeight: 'bold',
          fontSize: '14px',
        }}
      >
        🧪 Test Sentry
      </button>
      <button
        onClick={testUnhandledError}
        style={{
          padding: '12px 24px',
          backgroundColor: '#e67e22',
          color: 'white',
          border: 'none',
          borderRadius: '6px',
          cursor: 'pointer',
          fontWeight: 'bold',
          fontSize: '14px',
        }}
      >
        💥 Test Unhandled Error
      </button>
    </div>
  );
}
