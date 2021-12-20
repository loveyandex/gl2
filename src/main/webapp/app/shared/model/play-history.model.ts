import dayjs from 'dayjs';
import { IGamer } from 'app/shared/model/gamer.model';

export interface IPlayHistory {
  id?: number;
  maxPlay?: number | null;
  datePlays?: number;
  playDate?: string;
  gamer?: IGamer | null;
}

export const defaultValue: Readonly<IPlayHistory> = {};
