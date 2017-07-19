using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web;
using System.Web.Mvc;
using ASP.NET_MVC.Models;
using System.Data.Entity.Infrastructure;
using System.Data.Entity.Core;

namespace ASP.NET_MVC.Controllers
{
    public class MedicamentoController : Controller
    {
        private FarmaVETEntities db = new FarmaVETEntities();

        // GET: /Medicamento/
        public ActionResult Index()
        {
            var medicamento = db.Medicamento.Include(m => m.Especie)
                .Include(m => m.Laboratorio)
                .Include(m => m.TipoMedicamento).OrderByDescending(m => m.MedicamentoId);
            return View(medicamento.ToList());
        }

        // GET: /Medicamento/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Medicamento medicamento = db.Medicamento.Find(id);
            if (medicamento == null)
            {
                return HttpNotFound();
            }
            return View(medicamento);
        }

        // GET: /Medicamento/Create
        public ActionResult Create()
        {
            ViewBag.EspecieId = new SelectList(db.Especie, "EspecieId", "Descripcion");
            ViewBag.LaboratorioId = new SelectList(db.Laboratorio, "LaboratorioId", "Descripcion");
            ViewBag.TipoMedicamentoId = new SelectList(db.TipoMedicamento, "TipoMedicamentoId", "Descripcion");
            return View();
        }

        // POST: /Medicamento/Create
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include="MedicamentoId,Nombre,EspecieId,TipoMedicamentoId,LaboratorioId,Descripcion,Precio,Stock,Estado")] Medicamento medicamento)
        {
            ViewBag.Especies = new SelectList(db.Especie, "EspecieId", "Descripcion", medicamento.EspecieId);
            ViewBag.Laboratorios = new SelectList(db.Laboratorio, "LaboratorioId", "Descripcion", medicamento.LaboratorioId);
            ViewBag.TipoMedicamentos = new SelectList(db.TipoMedicamento, "TipoMedicamentoId", "Descripcion", medicamento.TipoMedicamentoId);
            
            try
            {
                int registrado = db.SP_RegistrarMedicamento(
                        medicamento.Nombre,
                        medicamento.EspecieId,
                        medicamento.TipoMedicamentoId,
                        medicamento.LaboratorioId,
                        medicamento.Descripcion,
                        medicamento.Precio
                );
                if (registrado > 0)
                {
                    ViewBag.MessageSuccess = "Registro grabado!";
                    return View(medicamento);
                }
                return View(medicamento);
            }
            catch (EntityCommandExecutionException e)
            {
                if (e.InnerException.Message.Contains("NombreEspecieUK"))
                {
                    ViewBag.MessageError = "Ya existe medicamento!";
                }
                else if (e.InnerException.Message.Contains("FK") )
                {
                    ViewBag.MessageError = "No se olvide de seleccionar Especie, Laboratorio y Tipo Medicamento";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo guardar registro!";
                }
                return View(medicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(medicamento);
            }
        }

        // GET: /Medicamento/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Medicamento medicamento = db.Medicamento.Find(id);
            if (medicamento == null)
            {
                return HttpNotFound();
            }
            ViewBag.EspecieId = new SelectList(db.Especie, "EspecieId", "Descripcion", medicamento.EspecieId);
            ViewBag.LaboratorioId = new SelectList(db.Laboratorio, "LaboratorioId", "Descripcion", medicamento.LaboratorioId);
            ViewBag.TipoMedicamentoId = new SelectList(db.TipoMedicamento, "TipoMedicamentoId", "Descripcion", medicamento.TipoMedicamentoId);
            return View(medicamento);
        }

        // POST: /Medicamento/Edit/5
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include="MedicamentoId,Nombre,EspecieId,TipoMedicamentoId,LaboratorioId,Descripcion,Precio,Stock,Estado")] Medicamento medicamento)
        {
            ViewBag.EspecieId = new SelectList(db.Especie, "EspecieId", "Descripcion", medicamento.EspecieId);
            ViewBag.LaboratorioId = new SelectList(db.Laboratorio, "LaboratorioId", "Descripcion", medicamento.LaboratorioId);
            ViewBag.TipoMedicamentoId = new SelectList(db.TipoMedicamento, "TipoMedicamentoId", "Descripcion", medicamento.TipoMedicamentoId);
            try
            {
                int modificado = db.SP_ModificarMedicamento(
                        medicamento.MedicamentoId,
                        medicamento.Nombre,
                        medicamento.EspecieId,
                        medicamento.TipoMedicamentoId,
                        medicamento.LaboratorioId,
                        medicamento.Descripcion,
                        medicamento.Precio
                );
                if (modificado > 0)
                {
                    ViewBag.MessageSuccess = "Registro modificado!";
                    return View(medicamento);
                }
                return View(medicamento);
            }
            catch (EntityCommandExecutionException e)
            {
                if (e.InnerException.Message.Contains("NombreEspecieUK"))
                {
                    ViewBag.MessageError = "No puedes editar el nombre a uno existente!";
                }
                else if (e.InnerException.Message.Contains("FK"))
                {
                    ViewBag.MessageError = "No se olvide de seleccionar Especie, Laboratorio y Tipo Medicamento";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo editar registro!";
                }
                return View(medicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(medicamento);
            }
        }

        // GET: /Medicamento/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Medicamento medicamento = db.Medicamento.Find(id);
            if (medicamento == null)
            {
                return HttpNotFound();
            }
            return View(medicamento);
        }

        // POST: /Medicamento/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            Medicamento medicamento = db.Medicamento.Find(id);
            try
            {
                db.Medicamento.Remove(medicamento);
                db.SaveChanges();
                ViewBag.MessageSuccess = "Registro Eliminado";
                return View();
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("FK"))
                {
                    ViewBag.MessageError = "No se puede eliminar porque hay registros enlazados";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo eliminar!";
                }
                return View(medicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(medicamento);
            }
        }

        public ActionResult IngresarStock(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Medicamento medicamento = db.Medicamento.Find(id);
            if (medicamento == null)
            {
                return HttpNotFound();
            }
            return View(medicamento);
        }

        [HttpPost]
        public ActionResult IngresarStock(Medicamento medicamento)
        {
            try
            {
                if(medicamento.Stock == 0)
                {
                    ViewBag.MessageError = "Ingresa una cantidad mayor a 0!";
                }
                if (medicamento.Stock < 0)
                {
                    ViewBag.MessageError = "Ingresa un valor positivo";
                }
                if (medicamento.Stock > 0 && medicamento.MedicamentoId > 0)
                {
                    int ingresado = db.SP_AgregarStock(medicamento.MedicamentoId, medicamento.Stock);
                    if (ingresado > 0)
                    {
                        ViewBag.MessageSuccess = "Cantidad adicionada al stock!";
                    }
                    else
                    {
                        ViewBag.MessageSuccess = "No se pudo agregar stock!";
                    }
                }
                return View(medicamento);
            }
            catch (EntityCommandExecutionException)
            {
                ViewBag.MessageError = "Ya ha excedido el maximo valor permitido!";
                return View(medicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(medicamento);
            }
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
