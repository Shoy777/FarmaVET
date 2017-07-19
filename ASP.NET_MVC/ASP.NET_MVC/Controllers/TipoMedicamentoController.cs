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
using System.Data.SqlClient;

namespace ASP.NET_MVC.Controllers
{
    public class TipoMedicamentoController : Controller
    {
        private FarmaVETEntities db = new FarmaVETEntities();

        // GET: /TipoMedicamento/
        public ActionResult Index()
        {
            return View(db.TipoMedicamento.ToList().OrderByDescending(t => t.TipoMedicamentoId));
        }

        // GET: /TipoMedicamento/Details/5
        public ActionResult Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            TipoMedicamento tipomedicamento = db.TipoMedicamento.Find(id);
            if (tipomedicamento == null)
            {
                return HttpNotFound();
            }
            return View(tipomedicamento);
        }

        // GET: /TipoMedicamento/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: /TipoMedicamento/Create
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create([Bind(Include="TipoMedicamentoId,Descripcion")] TipoMedicamento tipomedicamento)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.TipoMedicamento.Add(tipomedicamento);
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Grabado";
                    return View();
                }
                return View(tipomedicamento);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionTipoMedicamentoUK"))
                {
                    ViewBag.MessageError = "Ya existe tipo medicamento con el mismo nombre";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo grabar registro!";
                }
                return View(tipomedicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(tipomedicamento);
            }
        }

        // GET: /TipoMedicamento/Edit/5
        public ActionResult Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            TipoMedicamento tipomedicamento = db.TipoMedicamento.Find(id);
            if (tipomedicamento == null)
            {
                return HttpNotFound();
            }
            return View(tipomedicamento);
        }

        // POST: /TipoMedicamento/Edit/5
        // Para protegerse de ataques de publicación excesiva, habilite las propiedades específicas a las que desea enlazarse. Para obtener 
        // más información vea http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit([Bind(Include="TipoMedicamentoId,Descripcion")] TipoMedicamento tipomedicamento)
        {
            try
            {
                if (ModelState.IsValid)
                {
                    db.Entry(tipomedicamento).State = EntityState.Modified;
                    db.SaveChanges();
                    ViewBag.MessageSuccess = "Registro Modificado";
                    return View();
                }
                return View(tipomedicamento);
            }
            catch (DbUpdateException e)
            {
                if (e.InnerException.InnerException.Message.Contains("DescripcionTipoMedicamentoUK"))
                {
                    ViewBag.MessageError = "No puedes editar el nombre a uno existente!";
                }
                else
                {
                    ViewBag.MessageError = "No se pudo editar registro!";
                }
                return View(tipomedicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(tipomedicamento);
            }
        }

        // GET: /TipoMedicamento/Delete/5
        public ActionResult Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            TipoMedicamento tipomedicamento = db.TipoMedicamento.Find(id);
            if (tipomedicamento == null)
            {
                return HttpNotFound();
            }
            return View(tipomedicamento);
        }

        // POST: /TipoMedicamento/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public ActionResult DeleteConfirmed(int id)
        {
            TipoMedicamento tipomedicamento = db.TipoMedicamento.Find(id);
            try
            {
                db.TipoMedicamento.Remove(tipomedicamento);
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
                return View(tipomedicamento);
            }
            catch (Exception e)
            {
                ViewBag.MessageError = e.Message;
                return View(tipomedicamento);
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
