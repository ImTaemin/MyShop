function throttle(callback, delay) {
  let timeout = null;
  let lastCall = 0;

  return function (...args) {
    const now = new Date().getTime();
    const timeDiff = now - lastCall;
    if (!lastCall || timeDiff >= delay) {
      lastCall = now;
      callback(...args);
    } else {
      clearTimeout(timeout);
      timeout = setTimeout(() => {
        lastCall = now;
        callback(...args);
      }, delay - timeDiff);
    }
  };
}

export default throttle;