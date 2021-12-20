import { IGamer } from 'app/shared/model/gamer.model';

export interface IGame {
  id?: number;
  name?: string | null;
  gamers?: IGamer[] | null;
}

export const defaultValue: Readonly<IGame> = {};
