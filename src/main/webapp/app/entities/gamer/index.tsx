import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Gamer from './gamer';
import GamerDetail from './gamer-detail';
import GamerUpdate from './gamer-update';
import GamerDeleteDialog from './gamer-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GamerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GamerUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GamerDetail} />
      <ErrorBoundaryRoute path={match.url} component={Gamer} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GamerDeleteDialog} />
  </>
);

export default Routes;
