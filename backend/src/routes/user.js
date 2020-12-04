const express = require('express');
const {body} = require('express-validator');
const multer = require('multer');

const userController = require('../controllers/user');
const validate = require('../middlewares/unused/validate');

const router = express.Router();

const upload = multer().single('profileImage');



router.post('/createUser',[
    body('userName').not().isEmpty().withMessage('Your username is required'),
    body('password').not().isEmpty().withMessage('Your password is required')], userController.createUser)

router.get('/findAllUsers',[],userController.findAllUsers)


router.get('/delAllUsers',[],userController.delAllUsers)

router.post('/checkPasswordMatch',[
    body('password').not().isEmpty().withMessage('Your password is required')],userController.checkPasswordMatch)

/*
//INDEX
router.get('/', userController.index);


//STORE
router.post('/', [
    check('email').isEmail().withMessage('Enter a valid email address'),
    check('username').not().isEmpty().withMessage('You username is required'),
    check('firstName').not().isEmpty().withMessage('You first name is required'),
    check('lastName').not().isEmpty().withMessage('You last name is required')
], validate, User.store);

//SHOW
router.get('/:id',  User.show);

//UPDATE
router.put('/:id', upload, User.update);

//DELETE
router.delete('/:id', User.destroy);
*/

module.exports = router;