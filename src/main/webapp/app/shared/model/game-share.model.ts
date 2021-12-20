import dayjs from 'dayjs';
import { IGamer } from 'app/shared/model/gamer.model';

export interface IGameShare {
  id?: number;
  maxPlay?: number | null;
  shareTime?: string;
  gamer?: IGamer | null;
}

export const defaultValue: Readonly<IGameShare> = {};
