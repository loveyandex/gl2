import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPlayHistory, defaultValue } from 'app/shared/model/play-history.model';

export const ACTION_TYPES = {
  FETCH_PLAYHISTORY_LIST: 'playHistory/FETCH_PLAYHISTORY_LIST',
  FETCH_PLAYHISTORY: 'playHistory/FETCH_PLAYHISTORY',
  CREATE_PLAYHISTORY: 'playHistory/CREATE_PLAYHISTORY',
  UPDATE_PLAYHISTORY: 'playHistory/UPDATE_PLAYHISTORY',
  PARTIAL_UPDATE_PLAYHISTORY: 'playHistory/PARTIAL_UPDATE_PLAYHISTORY',
  DELETE_PLAYHISTORY: 'playHistory/DELETE_PLAYHISTORY',
  RESET: 'playHistory/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPlayHistory>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type PlayHistoryState = Readonly<typeof initialState>;

// Reducer

export default (state: PlayHistoryState = initialState, action): PlayHistoryState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PLAYHISTORY_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PLAYHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PLAYHISTORY):
    case REQUEST(ACTION_TYPES.UPDATE_PLAYHISTORY):
    case REQUEST(ACTION_TYPES.DELETE_PLAYHISTORY):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PLAYHISTORY):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PLAYHISTORY_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PLAYHISTORY):
    case FAILURE(ACTION_TYPES.CREATE_PLAYHISTORY):
    case FAILURE(ACTION_TYPES.UPDATE_PLAYHISTORY):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PLAYHISTORY):
    case FAILURE(ACTION_TYPES.DELETE_PLAYHISTORY):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLAYHISTORY_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PLAYHISTORY):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PLAYHISTORY):
    case SUCCESS(ACTION_TYPES.UPDATE_PLAYHISTORY):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PLAYHISTORY):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PLAYHISTORY):
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

const apiUrl = 'api/play-histories';

// Actions

export const getEntities: ICrudGetAllAction<IPlayHistory> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PLAYHISTORY_LIST,
  payload: axios.get<IPlayHistory>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IPlayHistory> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PLAYHISTORY,
    payload: axios.get<IPlayHistory>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPlayHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PLAYHISTORY,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPlayHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PLAYHISTORY,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPlayHistory> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PLAYHISTORY,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPlayHistory> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PLAYHISTORY,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
