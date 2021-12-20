import { IUser } from 'app/shared/model/user.model';
import { IPlayHistory } from 'app/shared/model/play-history.model';
import { IGameShare } from 'app/shared/model/game-share.model';
import { IGame } from 'app/shared/model/game.model';

export interface IGamer {
  id?: number;
  name?: string | null;
  phonenumber?: string;
  verifyCode?: string | null;
  referalCode?: string | null;
  score?: number | null;
  canplayGameToday?: boolean | null;
  user?: IUser | null;
  playhistories?: IPlayHistory[] | null;
  gameShares?: IGameShare[] | null;
  inviter?: IGamer | null;
  invtings?: IGamer[] | null;
  games?: IGame[] | null;
}

export const defaultValue: Readonly<IGamer> = {
  canplayGameToday: false,
};
