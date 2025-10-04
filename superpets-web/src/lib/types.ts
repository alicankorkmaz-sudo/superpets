export interface User {
  uid: string;
  email: string;
  credits: number;
  createdAt: number;
}

export interface EditImageRequest {
  hero_id: string;
  image_url: string;
  num_images: number;
  output_format: 'jpeg' | 'png';
}

export interface ImageFile {
  url: string;
}

export interface EditImageResponse {
  images: ImageFile[];
  description: string;
}

export interface EditHistory {
  id: string;
  userId: string;
  prompt: string;
  inputImages: string[];
  outputImages: string[];
  creditsCost: number;
  timestamp: number;
}

export interface Transaction {
  userId: string;
  amount: number;
  type: 'PURCHASE' | 'DEDUCTION' | 'REFUND' | 'BONUS';
  description: string;
  timestamp: number;
}

export interface CreditBalance {
  credits: number;
}

export interface Hero {
  id: string;
  hero: string;
  identity: string;
  scene_options: string[];
}

export interface HeroesResponse {
  classics: Hero[];
  uniques: Hero[];
}


