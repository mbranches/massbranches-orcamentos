function FormSubmitButtom({ label }) {
    return (
        <button type="submit" className='w-full py-4 px-10 md:py-2 md:w-auto border border-slate-300 hover:border-slate-400 rounded-lg text-slate-700 outline-none cursor-pointer'>
            {label}
        </button>
    );
}

export default FormSubmitButtom;