import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IGamer, defaultValue } from 'app/shared/model/gamer.model';

export const ACTION_TYPES = {
  FETCH_GAMER_LIST: 'gamer/FETCH_GAMER_LIST',
  FETCH_GAMER: 'gamer/FETCH_GAMER',
  CREATE_GAMER: 'gamer/CREATE_GAMER',
  UPDATE_GAMER: 'gamer/UPDATE_GAMER',
  PARTIAL_UPDATE_GAMER: 'gamer/PARTIAL_UPDATE_GAMER',
  DELETE_GAMER: 'gamer/DELETE_GAMER',
  RESET: 'gamer/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IGamer>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type GamerState = Readonly<typeof initialState>;

// Reducer

export default (state: GamerState = initialState, action): GamerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_GAMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_GAMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_GAMER):
    case REQUEST(ACTION_TYPES.UPDATE_GAMER):
    case REQUEST(ACTION_TYPES.DELETE_GAMER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_GAMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_GAMER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_GAMER):
    case FAILURE(ACTION_TYPES.CREATE_GAMER):
    case FAILURE(ACTION_TYPES.UPDATE_GAMER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_GAMER):
    case FAILURE(ACTION_TYPES.DELETE_GAMER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAMER_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_GAMER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_GAMER):
    case SUCCESS(ACTION_TYPES.UPDATE_GAMER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_GAMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_GAMER):
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

const apiUrl = 'api/gamers';

// Actions

export const getEntities: ICrudGetAllAction<IGamer> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_GAMER_LIST,
  payload: axios.get<IGamer>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IGamer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_GAMER,
    payload: axios.get<IGamer>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IGamer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_GAMER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IGamer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_GAMER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IGamer> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_GAMER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IGamer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_GAMER,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
