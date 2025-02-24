const maxFileSize = 1024 * 1024 * 256

export const fileToBase64Stream = async (file: File): Promise<string> => {
  if (file.size > maxFileSize) {
    throw new Error(`File too big: ${file.size} > ${maxFileSize} bytes`)
  }
  const stream = file.stream();
  const reader = stream.getReader();
  const result = new Uint8Array(file.size);
  let offset = 0

  while (true) {
    const { done, value } = await reader.read();
    if (done) break;

    result.set(value, offset)
    offset += value.length
  }

  return btoa(result.reduce(function (data, byte) {
    return data + String.fromCharCode(byte);
  }, ''));
};

export const base64ToFile = (base64: string, filename: string): void => {
  const byteCharacters = atob(base64);
  const byteNumbers = Array.from(byteCharacters, char => char.charCodeAt(0));
  const byteArray = new Uint8Array(byteNumbers);
  const blob = new Blob([byteArray], { type: 'application/octet-stream' });
  const blobUrl = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = blobUrl;
  link.download = filename;
  link.click();
};
