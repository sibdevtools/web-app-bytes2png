export const encodeTextToBase64 = (text: string, encoding: string): string => {
  const encoder = new TextEncoder();
  const encoded = encoder.encode(text);
  return btoa(String.fromCharCode(...encoded));
};

export const decodeBase64ToText = (base64: string, encoding: string): string => {
  const decoded = atob(base64);
  const decoder = new TextDecoder();
  return decoder.decode(Uint8Array.from(decoded.split(''), c => c.charCodeAt(0)));
};

export const fileToBase64 = (file: File, callback: (result: string) => void): void => {
  const reader = new FileReader();
  reader.readAsDataURL(file);
  reader.onload = () => {
    callback(reader.result as string);
  };
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
