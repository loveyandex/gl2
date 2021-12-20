import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Game from './game';
import GameDetail from './game-detail';
import GameUpdate from './game-update';
import GameDeleteDialog from './game-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GameUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GameDetail} />
      <ErrorBoundaryRoute path={match.url} component={Game} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GameDeleteDialog} />
  </>
);

export default Routes;
