import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGame, defaultValue } from 'app/shared/model/game.model';

export const ACTION_TYPES = {
  FETCH_GAME_LIST: 'game/FETCH_GAME_LIST',
  FETCH_GAME: 'game/FETCH_GAME',
  CREATE_GAME: 'game/CREATE_GAME',
  UPDATE_GAME: 'game/UPDATE_GAME',
  PARTIAL_UPDATE_GAME: 'game/PARTIAL_UPDATE_GAME',
  DELETE_GAME: 'game/DELETE_GAME',
  RESET: 'game/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGame>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type GameState = Readonly<typeof initialState>;

// Reducer

export default (state: GameState = initialState, action): GameState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GAME_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GAME):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GAME):
    case REQUEST(ACTION_TYPES.UPDATE_GAME):
    case REQUEST(ACTION_TYPES.DELETE_GAME):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_GAME):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GAME_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GAME):
    case FAILURE(ACTION_TYPES.CREATE_GAME):
    case FAILURE(ACTION_TYPES.UPDATE_GAME):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_GAME):
    case FAILURE(ACTION_TYPES.DELETE_GAME):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAME_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAME):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GAME):
    case SUCCESS(ACTION_TYPES.UPDATE_GAME):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_GAME):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GAME):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/games';

// Actions

export const getEntities: ICrudGetAllAction<IGame> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GAME_LIST,
  payload: axios.get<IGame>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IGame> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GAME,
    payload: axios.get<IGame>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGame> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GAME,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGame> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GAME,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IGame> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_GAME,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGame> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GAME,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
