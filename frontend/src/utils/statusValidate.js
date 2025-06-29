import { toast } from "react-toastify";

const statusValidate = (status) => {
    if (status === 401 || status === 403) {
        toast.error("Conexão expirada, por favor faça login novamente")
    } else if(status === 500) {
        toast.error("Ocorreu um erro interno, por favor tente novamente");
    }
}

export default statusValidate;