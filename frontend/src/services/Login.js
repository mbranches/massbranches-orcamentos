import api from './Api'

async function login(email, password) {
    try {
        const response = await api.post('/auth/login', {email, password});

        const token = response.data.accessToken;

        localStorage.setItem('token', token);
    } catch(error) {
        throw error;
    }
}

export default login;