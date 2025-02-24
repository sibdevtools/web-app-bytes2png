export function formatFileSize(fileSizeInBytes: number): string {
  const kbFileSize = fileSizeInBytes / 1024;
  const mbFileSize = kbFileSize / 1024;

  if (mbFileSize >= 0.01) {
    return `${mbFileSize.toFixed(2)} MB`;
  } else if (kbFileSize >= 0.01) {
    return `${kbFileSize.toFixed(2)} KB`;
  } else {
    return `${fileSizeInBytes} bytes`;
  }
}
