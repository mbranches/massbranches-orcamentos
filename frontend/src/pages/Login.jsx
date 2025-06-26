import { useForm } from "react-hook-form";
import Logo from "../components/Logo"
import login from "../services/Login";
import FormTextField from "../components/FormTextField";
import { useState } from "react";
import LoadingScreen from "../components/LoadingScreen";
import { useNavigate } from "react-router-dom";

function Login() {
    const [loginError, setLoginError] = useState(null);

    const [loading, setLoading] = useState(false);

    const { register, handleSubmit, formState: { errors }} = useForm();

    const navigate = useNavigate();

    const onSubmit = async (data) => {
        setLoading(true);
        try {
            await login(data.email, data.password);

            navigate("/home")
        } catch(error) {
            const status = error?.response?.status;

            if (status === 401) {
                setLoginError("Email ou senha incorretos  .");
            } else {
                setLoginError("Erro interno no servidor. Tente novamente mais tarde.");
            }
        } finally {
            setLoading(false);
        }
    };

    const showRequiredErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Campo obrigatório.</p>
    );

    const showInvalidEmailErrorMessage = () => (
        <p className='text-red-500 -mt-3 text-[13px]'>Digite um email válido.</p>
    );
    
    return (
        <div className="w-full min-h-screen flex justify-center items-center bg-slate-200">
            <div className="flex items-center w-full min-h-screen sm:w-1/2 sm:py-10 sm:min-h-auto lg:w-2/5 bg-white px-10 rounded-lg">
                <div className="h-full w-full">
                    <div className="space-y-5">
                        <div className="space-y-4">
                            <div>
                                <Logo />
                            </div>

                            <div className="">
                                <h2 className="text-2xl font-bold">Fazer Login</h2>
                            </div>
                        </div>

                        <form onSubmit={handleSubmit(onSubmit)} className='space-y-6'>
                            <div>    
                                <FormTextField
                                    type={"email"}
                                    id={"email"}
                                    label={"Email"} 
                                    placeholder={"email@dominio.com"} 
                                    register={register('email', {
                                            required: true,
                                            pattern: {
                                                value: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/
                                            }
                                        })
                                    } 
                                />
                            </div>

                            {errors?.email?.type === "required" && showRequiredErrorMessage() || errors?.email?.type === "pattern" && showInvalidEmailErrorMessage()}

                            <div>    
                                <FormTextField
                                    type={"password"}
                                    id={"password"}
                                    label={"Senha"} 
                                    placeholder={"Digite sua senha"} 
                                    register={register('password', {
                                            required: true,
                                        })
                                    } 
                                />
                            </div>

                            {errors?.password?.type === "required" && showRequiredErrorMessage()}

                            <div>
                                <p className='text-red-500 -mt-3 text-[13px]'>{loginError}</p>
                            </div>


                            <div className="mt-10">
                                <button type="submit" className='w-full py-3 bg-blue-700 text-slate-100 rounded-lg hover:bg-blue-800 outline-none cursor-pointer'>Entrar</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            {loading && <LoadingScreen />}
        </div>
    );
}

export default Login;