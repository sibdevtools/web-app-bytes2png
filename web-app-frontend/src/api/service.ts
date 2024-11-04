import axios from 'axios';

const service = axios.create({
  baseURL: '/web/app/bytes2png/rest/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

export interface EncodeRq {
  width: number | null;
  height: number | null;
  content: string;
}

export interface ServiceRs {
  success: boolean;
  body: string;
}

// BYTES2PNG encode
export const encode = (rq: EncodeRq) => service.post<ServiceRs>('/v1/encode', rq);

export interface DecodeRq {
  content: string;
}

// BYTES2PNG encode
export const decode = (rq: DecodeRq) => service.post<ServiceRs>('/v1/decode', rq);
