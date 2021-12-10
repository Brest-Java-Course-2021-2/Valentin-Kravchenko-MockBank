const modal = document.querySelector('#staticBackdrop');
if (modal) {
    const modalLabel = document.querySelector('#staticBackdropLabel')
    const modalBody = document.querySelector('.modal-body')
    const form = document.querySelector('#modalForm');
    const submit = document.querySelector('#modalSubmit');
    modal.addEventListener('show.bs.modal', (event) => {
        const link = event.relatedTarget;
        modalLabel.textContent = 'Do you want to remove the ' + link.dataset.entity + '?';
        modalBody.textContent = link.dataset.number;
        form.action = '/' + link.dataset.entity + '/' + link.dataset.id + '/remove';
        submit.addEventListener("click", () => form.submit());
    });
}

const submitCreateCardForm = (id, number) => {
    const form = document.querySelector("#createCardForm");
    const inputAccountId = document.querySelector("input[name='accountId']");
    const inputAccountNumber = document.querySelector("input[name='accountNumber']");
    inputAccountId.value = id;
    inputAccountNumber.value = number;
    form.submit();
}