import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GameShare from './game-share';
import GameShareDetail from './game-share-detail';
import GameShareUpdate from './game-share-update';
import GameShareDeleteDialog from './game-share-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GameShareUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GameShareUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GameShareDetail} />
      <ErrorBoundaryRoute path={match.url} component={GameShare} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GameShareDeleteDialog} />
  </>
);

export default Routes;
