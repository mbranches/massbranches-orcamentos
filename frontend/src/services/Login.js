import api from './Api'

async function getToken(email, password) {
    try {
        const response = await api.post('/auth/login', {email, password});

        return response.data.accessToken;
    } catch(error) {
        throw error;
    }
}

export default getToken;