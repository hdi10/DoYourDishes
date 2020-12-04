const auth = require('./auth');
const user = require('./user');
const plan = require('./plan');
const task = require('./task');

const authenticate = require('../middlewares/unused/authenticate');

module.exports = app => {
    app.get('/', (req, res) => {
        res.status(200).send({ message: "Welcome to the AUTHENTICATION API. Register or Login to test Authentication."});
    });

    //app.use('/api/auth', auth);
   // app.use('/api/user', authenticate, user);
    app.use('/api/plan', plan);
    app.use('/api/task', task);
    app.use('/api/user', user);
    app.use('/api/auth', auth);
};