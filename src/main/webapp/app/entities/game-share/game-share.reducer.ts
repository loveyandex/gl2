import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGameShare, defaultValue } from 'app/shared/model/game-share.model';

export const ACTION_TYPES = {
  FETCH_GAMESHARE_LIST: 'gameShare/FETCH_GAMESHARE_LIST',
  FETCH_GAMESHARE: 'gameShare/FETCH_GAMESHARE',
  CREATE_GAMESHARE: 'gameShare/CREATE_GAMESHARE',
  UPDATE_GAMESHARE: 'gameShare/UPDATE_GAMESHARE',
  PARTIAL_UPDATE_GAMESHARE: 'gameShare/PARTIAL_UPDATE_GAMESHARE',
  DELETE_GAMESHARE: 'gameShare/DELETE_GAMESHARE',
  RESET: 'gameShare/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGameShare>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type GameShareState = Readonly<typeof initialState>;

// Reducer

export default (state: GameShareState = initialState, action): GameShareState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GAMESHARE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GAMESHARE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GAMESHARE):
    case REQUEST(ACTION_TYPES.UPDATE_GAMESHARE):
    case REQUEST(ACTION_TYPES.DELETE_GAMESHARE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_GAMESHARE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GAMESHARE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GAMESHARE):
    case FAILURE(ACTION_TYPES.CREATE_GAMESHARE):
    case FAILURE(ACTION_TYPES.UPDATE_GAMESHARE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_GAMESHARE):
    case FAILURE(ACTION_TYPES.DELETE_GAMESHARE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAMESHARE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAMESHARE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GAMESHARE):
    case SUCCESS(ACTION_TYPES.UPDATE_GAMESHARE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_GAMESHARE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GAMESHARE):
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

const apiUrl = 'api/game-shares';

// Actions

export const getEntities: ICrudGetAllAction<IGameShare> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GAMESHARE_LIST,
  payload: axios.get<IGameShare>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IGameShare> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GAMESHARE,
    payload: axios.get<IGameShare>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGameShare> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GAMESHARE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGameShare> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GAMESHARE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IGameShare> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_GAMESHARE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGameShare> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GAMESHARE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
